package sjunejo.com.musictastefriendfinder.api;

/**
 * Created by sjunjo on 22/04/2017.
 */

public class Artist {

    private String artistName;
    private String[] artistGenres;

    public Artist(){

    }

    public Artist(String artistName, String[] artistGenres){
        this.artistName = artistName;
        this.artistGenres = artistGenres;
    }
}
