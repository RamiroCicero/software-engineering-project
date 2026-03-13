package it.polimi.ingsw.model.Board;


import com.google.gson.Gson;
import it.polimi.ingsw.model.PersonalCards.PersonalParser;

import java.io.*;

/**
 * Parses a JSON file containing board mask configurations and provides access to the parsed BoardMask object.
 */
public class BoardMaskParser implements Serializable {

    private final BoardMask boardMask;

    /**
     * Constructs a BoardMaskParser and parses the board mask configurations from a JSON file.
     *
     * @throws FileNotFoundException If the JSON file cannot be found.
     */
    public BoardMaskParser() throws FileNotFoundException {
        InputStream inputStream = PersonalParser.class.getResourceAsStream("/json/boardMasks.json");
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonContent=new StringBuilder();
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            jsonContent.append(line);
        }
        Gson gson = new Gson();
        boardMask = gson.fromJson(jsonContent.toString(), BoardMask.class);
    }

    /**
     * Retrieves the parsed BoardMask object.
     *
     * @return The parsed BoardMask object.
     */
    public BoardMask getBoardMask() {
        return boardMask;
    }
}
