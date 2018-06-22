package Data;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class genderList {

    ArrayList<movieGender> genres;

    public ArrayList<movieGender> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<movieGender> genres) {
        this.genres = genres;
    }
}
