package com.jarhax.eyespy;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataManager {
    
    private final Path dataFile;
    private final Yaml yaml;
    private final Map<UUID, UserData> userData = new ConcurrentHashMap<>();
    
    public UserDataManager(Path dataDirectory) {
        this.dataFile = dataDirectory.resolve("users.yml");
        
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        this.yaml = new Yaml(options);
        
        if (!Files.exists(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create data directory", e);
            }
        }
        
        loadData();
    }
    
    private void loadData() {
        if (!Files.exists(dataFile)) {
            saveData();
            return;
        }
        
        try {
            Map<String, Object> data = yaml.load(Files.newInputStream(dataFile));
            if (data == null || data.isEmpty()) {
                return;
            }
            
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                UUID uuid = UUID.fromString(entry.getKey());
                Map<String, Object> userData = (Map<String, Object>) entry.getValue();
                String name = (String) userData.get("name");
                boolean enabled = userData.getOrDefault("enabled", true) instanceof Boolean 
                    ? (Boolean) userData.get("enabled") 
                    : true;
                
                this.userData.put(uuid, new UserData(name, enabled));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load user data", e);
        }
    }
    
    private void saveData() {
        Map<String, Map<String, Object>> data = new HashMap<>();
        
        for (Map.Entry<UUID, UserData> entry : userData.entrySet()) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", entry.getValue().name);
            userMap.put("enabled", entry.getValue().enabled);
            data.put(entry.getKey().toString(), userMap);
        }
        
        try {
            Files.writeString(dataFile, yaml.dump(data));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user data", e);
        }
    }
    
    public boolean isEnabled(UUID uuid, String name) {
        UserData data = userData.get(uuid);
        if (data == null) {
            data = new UserData(name, true);
            userData.put(uuid, data);
            saveData();
            return true;
        }
        
        if (!data.name.equals(name)) {
            data.name = name;
            saveData();
        }
        
        return data.enabled;
    }
    
    public void setEnabled(UUID uuid, String name, boolean enabled) {
        UserData data = userData.computeIfAbsent(uuid, k -> new UserData(name, enabled));
        data.enabled = enabled;
        data.name = name;
        saveData();
    }
    
    public boolean toggleEnabled(UUID uuid, String name) {
        UserData data = userData.computeIfAbsent(uuid, k -> new UserData(name, true));
        data.enabled = !data.enabled;
        data.name = name;
        saveData();
        return data.enabled;
    }
    
    private static class UserData {
        String name;
        boolean enabled;
        
        UserData(String name, boolean enabled) {
            this.name = name;
            this.enabled = enabled;
        }
    }
}
