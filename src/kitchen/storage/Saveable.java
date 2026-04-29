package kitchen.storage;

import java.io.IOException;

public interface Saveable {

    void save(String filePath) throws IOException;

    void load(String filePath) throws IOException;

}
