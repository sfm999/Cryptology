
This ransomware program and subsequent recovery program only require that
Python 3.5 or above and pycryptodome package are installed.
No further packages are required for the program to run.

Simple Run Instructions:

    1) Open 'virus' folder and place any '.py' and '.txt' files in there too. At
       least one of each.

    2) Run 'virus.py' from 'virus' folder directory through Terminal.

    3) To recover, place the encoded documents in to the recovery folder you are
        sent via email, then run 'python3 recovery.py' from the 'Recovery' directory in
        Terminal.

Detailed Report

I had a bit of trouble figuring out how to present the program, so I figured
I'd represent the assignment structure in the same format I had for testing.

I've included a prepared version that simulates the actual target environment
and my environment. In this version, the RSA keys are already generated and
distributed to their respective environments.

The program has a number of stages:

1) Preparation
    - 'rsa-key-gen.py' is run in the attackers environment. It generates
      the public and private key, 'receiver.pem' & 'ransomprvkey.pem'.
      'receiver.pem' is sent to the target folder, and 'ransomprvkey.pem' is
      sent to the recovery program folder.

2) Dormant Phase
    - The target folder is loaded with the following files:
      - 'virus.py'
      - 'receiver.pem'
      - Some assortment of '.txt' files and '.py' files

3) Payload Phase
    Step three is a combination of the 'Propagating', 'Triggering', and 'Action'
    phases. This is because once the dormant phase is complete, when we run the
    'virus.py' file, it will trigger the action phase, which will not only encrypt
    all '.txt' files, but will also replicate (propagate) itself to the other '.py'
    files in the directory as well as paste the Initial Vector (IV) in plaintext at the bottom
    of each infected file. IV was also written to file as a way of hard coding
    the IV for decryption.

    The result will be that the target folders files change to the following:
        - 'virus.py'
        - 'receiver.pem'
        - 'key.bin'
        - 'iv.pem'
        - Encrypted versions of ''.txt' files and subsequent deletion of original
            '.txt' files.
        - All '.py' files have had their code commented out and the virus code pasted
          in.

    Also, a message will pop up on the users terminal saying:

    "Your text files are encrypted. To decrypt them, you need to pay me $5,000 and
     send key.bin in your folder sfm999@uowmail.edu.au"

4) Recovery Phase

  If the user pays the ransom, they are to send 'key.bin' to my email address.
  I will in return send them the recovery folder which will contain the following:
      - 'recovery.py'
      - 'ransomprvkey.py'
      - 'key.bin'
      - 'Readme.txt'

  Now, the reason I send key.bin back is because of the way the decryption Program
  is structured. Within the 'Readme.txt', there will be an explanation of how to
  run the decryption program. To put it simply, all you must do is put the
  contents of the recovery folder in to the affected folder, as well as 'iv.pem'
  and then run 'recovery.py'.This is because the 'recovery.py' program decrypts
  the key and then uses it to decrypt the encrypted '.txt' files.

  Conversely, you could place the contents of the 'Recovery' folder in to the
  affected folder. This may be easier but was not done so in testing.

  Contents of encoded files will be decoded and written to '.txt' files of the
  original name. 
