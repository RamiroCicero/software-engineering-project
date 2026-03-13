package it.polimi.ingsw.model.PersonalCards;

import com.google.gson.Gson;

import java.io.*;

/**
 * Represents a PersonalParser that handles the serialization and deserialization of Personal Cards.
 * This class provides functionality to read and access the Personal Cards from a JSON file.
 */
public class PersonalParser implements Serializable {

    /**
     * vector of all Personal Cards in game
     */
    CardPersonalTarget[] cardPersonalTargets ;

    /**
     * constructor that deserializes the JSON file: listOfPersonalCards.json
     * @throws FileNotFoundException if the file is not found
     */

    public PersonalParser() throws FileNotFoundException {
        InputStream inputStream = PersonalParser.class.getResourceAsStream("/json/listOfPersonalCards.json");
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
        cardPersonalTargets = gson.fromJson(jsonContent.toString(), CardPersonalTarget[].class);

    }

    /**
     * getter of cardPersonalTargets
     */
    public CardPersonalTarget[] getCardPersonalTargets() {
        return cardPersonalTargets;
    }
}
