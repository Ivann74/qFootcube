package me.qajic.plugins.qfootcube.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class UUIDConverter {
    private HashMap<String, UUID> values = new HashMap();
    private String path;
    private File file;

    public void setup(String s) {
        this.path = s;
        this.file = new File(this.path);
        if (!this.file.getAbsoluteFile().exists()) {
            try {
                this.file.createNewFile();
                this.save();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

    }

    public UUID get(String key) {
        return this.values.get(key);
    }

    public String getKey(UUID value) {
        Iterator var3 = this.values.keySet().iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            if (this.values.get(s).equals(value)) {
                return s;
            }
        }

        return null;
    }

    public boolean has(String key) {
        return this.values.containsKey(key);
    }

    public boolean hasValue(UUID value) {
        return this.values.containsValue(value);
    }

    public void put(String key, UUID value) {
        this.values.put(key, value);
        this.save();
    }

    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.path)));
            oos.writeObject(this.values);
            oos.flush();
            oos.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.path)));
            this.values.clear();
            HashMap<String, UUID> scoreMap = (HashMap)ois.readObject();
            this.values = scoreMap;
            ois.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}

