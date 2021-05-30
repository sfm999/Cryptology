
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class Modified_SHA1
{
  public static void main(String[] args)
  {
    SSHA1 s_sha1 = new SSHA1();
  }
}

class SSHA1
{
  String init_text = "The Cat-In-The-Hat owes ManBoy ";
  final int MAXIMUM = 100;

  public SSHA1()
  {
    try
    {
      long start_time = System.nanoTime();
      find_x_xi();
      long end_time = System.nanoTime();
      System.out.printf("Time taken to execute: %.2f seconds\n", (end_time - start_time)/1000000000.0);
    }catch(UnsupportedEncodingException ie)
    {
      System.out.println(ie);
    }catch(NoSuchAlgorithmException ne)
    {
      System.out.println(ne);
    }

  }

  void find_x_xi() throws UnsupportedEncodingException, NoSuchAlgorithmException
  {
    /*
      @return void find_x_xi()

        Code adapted from 'http://www.sha1-online.com/sha1-java/'

        Finds x such that x != x' but the SSHA1 hash values are the same.
        Find H(x) == H(x')

    */

    // BEGIN VARIABLES

    // Set up StringBuilders
    StringBuilder sb1 = new StringBuilder(), sb2 = new StringBuilder(),
                  sb3 = new StringBuilder(), sb4 = new StringBuilder();

    // Hold initial sentence with appendment
    String s1 = "", s2 = "";
    // Hold result post hash of sentence for comparison
    String res1 = "", res2 = "";

    // Create byte arrays that are used in the hashing process
    byte[] byte_res1, byte_res2;

    // Collision flag. While loop continues while true
    boolean no_collision = true;

    // i is sentence 1's 'x'. Count is the amount of attempts we have made finding x != xi
    int x = 0, count = 0;

    // Our instance of SHA1 using MessageDigest
    MessageDigest digestion = MessageDigest.getInstance("SHA1");

    // END VARIABLES


    // MAIN LOOP - Calculates x and xi, then compares. Break if collision found

    while(no_collision)
    {
      sb1.append(init_text + Integer.toString(x) + " dollars");
      s1 = sb1.toString();

      byte_res1 = digestion.digest(s1.getBytes());

      // Calculate the hash value of s1
      for(int w = 0; w < byte_res1.length; w++)
      {
        sb3.append(Integer.toString((byte_res1[w] & 0xff) + 0x100, 16).substring(1));
      }

      res1 = sb3.toString();
      res1 = res1.substring(0, 6);

      for(int y = 0; y < MAXIMUM; y++)
      {
        if(y != x)
        {
          // Increment count as this is an attempt being made now.
          ++count;

          // Build second string
          sb2.append(init_text + Integer.toString(y) + " dollars");
          s2 = sb2.toString();

          // Initialise byte arrays with s1 and s2. Run through digestion

          byte_res2 = digestion.digest(s2.getBytes());

          // Calculate the hash value of s2
          for(int z = 0; z < byte_res2.length; z++)
          {
            sb4.append(Integer.toString((byte_res2[z] & 0xff) + 0x100, 16).substring(1));
          }

          // Assign results to res1, res2

          res2 = sb4.toString();
          res2 = res2.substring(0, 6);

          if(res1.equals(res2))
          {
            no_collision = false;
          }
        }

        if(!no_collision)
        {
          y = MAXIMUM;
        }

        sb1.setLength(0);
        sb2.setLength(0);
        sb3.setLength(0);
        sb4.setLength(0);
      }
      x++;
    }
    System.out.printf("\n%s\nHash val of sentence with x: %s\n", s1, res1);
    System.out.printf("\n%s\nHash val of sentence with x': %s\n", s2, res2);
    System.out.println("\nNumber of attempts before a collision was found: " + count + "\n");
  }
}
