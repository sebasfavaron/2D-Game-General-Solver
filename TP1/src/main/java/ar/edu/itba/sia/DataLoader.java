package ar.edu.itba.sia;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLoader {
    private String file;
    private List<Integer> up, down, left, right;
    private int size;

    public DataLoader(String file) {
        this.file = file;
        BufferedReader bufferedReader;
        try{
            URL url = getClass().getClassLoader().getResource(file);
            //bufferedReader = new BufferedReader(new FileReader(url.getPath()));
            bufferedReader = new BufferedReader(new FileReader(Paths.get(file).toFile()));

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to open file");
            System.exit(-1);
            return;
        }

        Gson gson = new Gson();
        JsonObject js = gson.fromJson(bufferedReader, JsonObject.class);
        size = js.get("size").getAsInt();

        JsonObject restrictions = js.getAsJsonObject("restrictions");
        Type listType = new TypeToken<List<Integer>>() {}.getType();

        up    = new Gson().fromJson(restrictions.get("up"), listType);
        down  = new Gson().fromJson(restrictions.get("down"), listType);
        left  = new Gson().fromJson(restrictions.get("left"), listType);
        right = new Gson().fromJson(restrictions.get("right"), listType);

        checkLists();
    }

    private void checkLists() {
        List<List<Integer>> restrictions = Arrays.asList(up, down, left, right);

        for(List<Integer> l : restrictions) {
            if(l.size() != size) {
                System.out.println("[INVALID INPUT FILE] One of the restrictions array does not have size " + size);
                System.exit(0);
            }

            for(int index = 0; index < l.size(); index++) {
                if(l.get(index) == null) {
                    l.remove(index);
                    l.add(index,0);
                }

                int i = l.get(index);
                if(i < 0 || i > size) {
                    System.out.println("[INVALID INPUT FILE] Value " + i + " in restrictions is not between 0 and " + size);
                    System.exit(0);
                }
            }
        }
    }

    public List<Integer> getUp() {
        return new ArrayList<>(up);
    }

    public List<Integer> getDown() {
        return new ArrayList<>(down);
    }

    public List<Integer> getLeft() {
        return new ArrayList<>(left);
    }

    public List<Integer> getRight() {
        return new ArrayList<>(right);
    }
    public Restrictions getRestrictions(){
        return new Restrictions(up, right, down, left);
    }

    public int getSize() {
        return size;
    }
}
