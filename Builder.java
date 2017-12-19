package toascii;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/* So the goal here is to make an ascii picture out of
 * a jpeg or bmp or whatever you've got. We're using a 
 * resolution of one ascii character representing a 
 * 8x16 block of pixels, differing depending on the 
 * darkness of the top and bottom
 */
public class Builder {
	
	public static void main(String[] args) {
		String FYLE = "meta.png";
		float UPPER = 0.65f; // upper value of pixel darkness (increasing makes image 'lighter')
		int RES = 3; // horizontal resolution, min is 3, lower is fewer pixels lost
		int BLACK = (((int)(UPPER*RES*RES))/4) * 4; // darkness consequences
		int DIV = BLACK / 4; // 4 because 4x4 grid of pixel options
		BufferedImage img = null;
		FileWriter text = null;
		String[][] chars = new String[][] {
			{ " ", ".", "_", "," },
			{ "'", "I", "J", ";" },
			{ "\"", "T", "H", "A" },
			{ "^", "Y", "V", "B" }
		};
		try { // TODO: take in a filename
			img = ImageIO.read(new File(FYLE)); //filename is a command line argument
			text = new FileWriter(FYLE.split("[.]")[0] + ".txt", false); //filename.txt
		} catch (IOException e) {
			System.out.println("File failure");
			System.exit(1);
		}
		int height = img.getHeight();
		int blocksHigh = height / (RES * 2); // 8x16 pixels per character
		int width = img.getWidth();
		int blocksAcross = width / RES;
		for (int y = 0; y < blocksHigh; y++) { // Iterates across each unit
			for (int x = 0; x < blocksAcross; x++) {
				float topDark = 0;
				float bottomDark = 0;
				
				// adding up darkness in char block
				for (int i = 0; i < RES; i++) { // Within the top half of char
					for (int j = 0; j < RES; j++) {
						int pixel = img.getRGB(((x*RES)+j), ((y*RES*2)+i));
						topDark += darkness(pixel);
					}
				}
				for (int i = RES; i < RES*2; i++) { // Within bottom half of char
					for (int j = 0; j < RES; j++) {
						int pixel = img.getRGB(((x*RES)+j), ((y*RES*2)+i));
						bottomDark += darkness(pixel);
					}
				}
				
				// Choosing char based on top/bottom darkness
				int top = Math.min((int)(topDark), BLACK - 1) / DIV;
				int bottom = Math.min((int)(bottomDark), BLACK - 1) / DIV;
				try {
					text.write(chars[top][bottom]);
					if (x == blocksAcross - 1) { //reaching end of row
						text.write("\n");
					}
				} catch (IOException e) {
					System.out.println("Write failure");
					System.exit(1);
				}
			}
		}
		try { // Close created text file writer
			text.close();
		} catch (IOException e) {};
	}
	
	// Credits to Jack Herd (github.com/Soupdoop)
	// Gets darkness value, scales by cube to increase contrast
	// Lower represents min darkness value of pixel (increases 'darkness' of image)
	// Contrast is, well, contrast
	private static float darkness(int argb) {
		float CONTRAST = 3f;
		float LOWER = 0.1f;
		int blue = argb & 255;
	    int green = (argb >> 8) & 255;
	    int red = (argb >> 16) & 255;
	    float greyscale = 1 - (float)(blue + green + red) / 3 / 255;
	    return (LOWER + (1 - LOWER) * (float) Math.pow((double) greyscale, CONTRAST));
	}

}
