/*
 * MediaFactory - give out Media enumeration constants
 * @version $Id: MediaFactory.java,v 1.2 2004/03/20 20:50:09 ian Exp $
 */
public class MediaFactory {

	public static void main(String[] args) {
		
		System.out.println(MediaFactory.getMedia("Book"));
	}
	public static Media getMedia(String s) {
		return Enum.valueOf(Media.class, s.toLowerCase());
	}
	public static Media getMedia(int n){
		return Media.values()[n];
	}
}
