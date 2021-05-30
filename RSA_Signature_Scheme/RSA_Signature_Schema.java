
// Numbers
import java.math.BigInteger;
import java.security.SecureRandom;

// Files
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;

// Utilities
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Exceptions
import java.io.IOException;

// main
public class RSA_Signature_Schema
{
  public static void main(String[] args)
  {
    RSA_Signature_Scheme rsa_sig = new RSA_Signature_Scheme();
  }
}

// Q2 implementation
class RSA_Signature_Scheme
{
  // Global file readers
  public FileReader fr = null;
  public BufferedReader br = null;

  public FileWriter fw = null;
  public BufferedWriter bw = null;
  public Scanner reader = null;

  // Single digit pattern  and matcher for checking menu input
  Pattern p = Pattern.compile("[1-7]");
  Matcher m;

  public RSA_Signature_Scheme()
  {
    menu();
  }

  public void display_menu()
  {
    // Header and menu title
    String header = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
    System.out.println(header + "\n\tRSA Signature Simulation!\n" + header);

    // Menu options
    System.out.println("Choose from one of the following options: \n");
    System.out.println("1. Key Generation");
    System.out.println("2. Signature Generation");
    System.out.println("3. Verification");
    System.out.println("4. Key Generation with steps shown");
    System.out.println("5. Signature Generation with steps shown");
    System.out.println("6. Verification with steps shown");
    System.out.println("7. Exit");

    System.out.print("Your input was: ");

  }


  public void menu()
  {
    display_menu();
    reader = new Scanner(System.in);
    String temp = reader.next();
    m = p.matcher(temp);

    while (!m.matches())
    {
      System.out.println("Invalid input.");
      display_menu();
      temp = reader.next();
      m = p.matcher(temp);
    }

    int menu_choice = Integer.parseInt(temp);


    switch(menu_choice)
    {
      case 1:
        KeyGen(1);
        break;

      case 2:
        Sign(2);
        break;

      case 3:
        Verify(3);
        break;

      case 4:
        KeyGen(4);
        break;

      case 5:
        Sign(5);
        break;

      case 6:
        Verify(6);
        break;

      case 7:
        System.out.println("Exiting the program. Cya :)");
        System.exit(0);
        break;
    }
    menu();
  }

  public void KeyGen(int version)
  {
    // Generate our random for seed of finding primes
    SecureRandom sec_rand = new SecureRandom();
    // Generate primes p and k of 20-bit length each
    final int BIT_LENGTH = 20;
    BigInteger p = BigInteger.probablePrime(BIT_LENGTH, sec_rand);

    BigInteger q = null;

    do{
      q = BigInteger.probablePrime(BIT_LENGTH, sec_rand);
    }while(q == p);

    BigInteger n = p.multiply(q);
    BigInteger o_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

    BigInteger e = BigInteger.probablePrime(6, sec_rand);

    while(e.gcd(o_n).compareTo(BigInteger.ONE) != 0)
    {
      e = e.add(BigInteger.ONE);
    }


    BigInteger d = BigInteger.ONE;
    BigInteger temp = null;

    while(true)
    {
      d = e.modInverse(o_n);
      temp = e.multiply(d);
      temp = temp.mod(o_n);
      if(temp.compareTo(BigInteger.ONE) == 0)
      {
        break;
      }

    }

    // Write sk.txt
    try
    {
      fw = new FileWriter("sk.txt");
      bw = new BufferedWriter(fw);

      bw.write(d.toString());
      bw.newLine();
      bw.write(n.toString());

      bw.close();

    }catch(IOException fe)
    {
      System.out.println("Error writing to sk.txt");
    }

    // Write pk.txt
    try
    {
      fw = new FileWriter("pk.txt");
      bw = new BufferedWriter(fw);

      bw.write(e.toString());
      bw.newLine();
      bw.write(n.toString());

      bw.close();

    }catch(IOException fe)
    {
      System.out.println("Error writing to pk.txt");
    }

    BigInteger m = null;
    // Write mssg.txt
    try
    {
      fw = new FileWriter("mssg.txt");
      bw = new BufferedWriter(fw);

      m = new BigInteger(16, sec_rand);
      bw.write(m.toString());

      bw.close();

    }catch(IOException fe)
    {
      System.out.println("Error writing to mssg.txt");
    }

    // Optional printing based on menu choice (1 or 4)
    if(version == 4)
    {
      System.out.println("---------------------------------------------------");
      System.out.println("\tStep 1");
      System.out.println("---------------------------------------------------");
      System.out.println("\nGenerate primes p and q, 20 bits each in length.");
      System.out.printf("\tp: %s\n", p.toString());
      System.out.printf("\tq: %s\n\n", q.toString());

      System.out.println("---------------------------------------------------");
      System.out.println("\tStep 2");
      System.out.println("---------------------------------------------------");
      System.out.println("\nGenerate n as pq, 0(n) as (p-1)(q-1):\n");
      System.out.printf("\tn = %s\n", n.toString());
      System.out.printf("\tO(n) = %s\n", o_n.toString());

      System.out.println("---------------------------------------------------");
      System.out.println("\tStep 3");
      System.out.println("---------------------------------------------------");
      System.out.println("\nGenerate e such that gcd(e, 0(n) equals 1:");
      System.out.printf("\tgcd(%s, %s) == %s\n\n", e.toString(), o_n.toString(), e.gcd(o_n).toString());

      System.out.println("---------------------------------------------------");
      System.out.println("\tStep 4");
      System.out.println("---------------------------------------------------");
      System.out.println("Generate d such that ed = 1(mod 0(n)).");
      System.out.printf("\t%s * %s = %s mod %s\n\n", e.toString(), d.toString(), ((e.multiply(d)).mod(o_n)).toString() , o_n.toString());

      System.out.println("---------------------------------------------------");
      System.out.println("\tStep 5 (sort of)");
      System.out.println("---------------------------------------------------");
      System.out.println("\nGenerate m such that it is less than n.");
      System.out.printf("\tm = %s\n\n", m.toString());

      System.out.println("\nThe keys: \n");
      System.out.println("\tSecret key = (" + d + ", " + n + ")");
      System.out.println("\tPublic key = (" + e + ", " + n + ")\n");
    }
  }

  public void Sign(int version)
  {
    /*
      @return void generate_signature()

        - Reads in the message, and the secrety key from sk.txt.
        - Calculates s such that s = m^d(mod n).
        - Write s to sig.txt.
    */

    // Declare variables (m, d, n, s)
    BigInteger m = null, d = null, n = null, s = null;

    // Read in m
    try
    {
      fr = new FileReader("mssg.txt");
      br = new BufferedReader(fr);

      m = new BigInteger(br.readLine());

      br.close();
    }catch(IOException ioe)
    {
      System.out.println("Error reading file mssg.txt. Check if file exists before trying again.");
    }

    // Read in sk
    try
    {
      fr = new FileReader("sk.txt");
      br = new BufferedReader(fr);

      d = new BigInteger(br.readLine());
      n = new BigInteger(br.readLine());

      br.close();

    }catch(IOException ioe)
    {
      System.out.println("Error reading file sk.txt. Check if file exists before trying again.");
    }

    // Perform calculation s = m^d (mod n)
    s = m.modPow(d, n);

    //Write s to sig.txt
    try
    {
      fw = new FileWriter("sig.txt");
      bw = new BufferedWriter(fw);

      bw.write(s.toString());

      bw.close();
    }catch(IOException ioe)
    {
      System.out.println("Error writing to file sig.txt.");
    }

    // Optional printing of process based on menu choice
    if(version == 5)
    {
      System.out.println("The process of generating a signature is as follows:\n");
      System.out.println("- Read in m | " + m.toString());
      System.out.println("- Read in sk(d, n) | (" + d.toString() + ", " + n.toString() + ")");
      System.out.println("- Compute s such that s = m^d(mod n).");
      System.out.println("\t" + m.toString() + "^ " + d.toString() + " (mod " + n + ")");
      System.out.println("\t\t= " + s.toString() + " (mod " + n.toString() + ")");
    }
  }

  public void Verify(int version)
  {
    /*
      @return void Verify()

        - Reads public key(e, n) from pk.txt.
        - Reads in s from sig.txt.
        - Reads in m from mssg.txt.
        - Performs calculation and comparison as follows:

              m = s^e(mod n).

          Hence, if m == s^e(mod n) then return true (verified)
          else false (not verified)
    */

    // Declare variables e, n, s, m, result
    BigInteger e = null, n = null, s = null, m = null, result = null;

    // Read pk.txt
    try
    {
      fr = new FileReader("pk.txt");
      br = new BufferedReader(fr);

      e = new BigInteger(br.readLine());
      n = new BigInteger(br.readLine());

      br.close();

    }catch(IOException ioe)
    {
      System.out.println("Error reading pk.txt. Check if file exists before trying again.");
    }

    // Read sig.txt
    try
    {
      fr = new FileReader("sig.txt");
      br = new BufferedReader(fr);

      s = new BigInteger(br.readLine());

      br.close();

    }catch(IOException ioe)
    {
      System.out.println("Error reading pk.txt. Check if file exists before trying again.");
    }

    // Read mssg.txt
    try
    {
      fr = new FileReader("mssg.txt");
      br = new BufferedReader(fr);

      m = new BigInteger(br.readLine());

      br.close();

    }catch(IOException ioe)
    {
      System.out.println("Error reading pk.txt. Check if file exists before trying again.");
    }

    result = s.modPow(e, n);
    System.out.print("\nVerified: ");
    if(m.compareTo(result) == 0)
    {
      System.out.println("True\n");
    }else{
      System.out.println("False\n");
    }

    if(version == 6)
    {
      System.out.println("The process of verifying a signature(s):\n");
      System.out.println("- Read in m | " + m.toString());
      System.out.println("- Read in pk(e, n) | (" + e.toString() + ", " + n.toString() + ")");
      System.out.println("- Compute m = s^e(mod n).");
      System.out.print("\t" + m.toString() + " = " + s.toString() + "^" + e.toString() + " (mod " + n + ")");
      System.out.println(" = " + result.toString());
      if(m.compareTo(result) == 0)
      {
        System.out.println("\n" + m.toString() + " = " + s.modPow(e, n).toString() + "\n");
        System.out.println("\nCongrats :)");
      }else{
        System.out.println("\nSomething went wrong. You probably generated keys but didn't sign the message.\nTry signing then re-do this step.");
        System.out.println("\nCurrently, your m is " + result.toString() + " instead of " + m.toString() + "\nWhoopsie!");
      }
    }
  }

}
