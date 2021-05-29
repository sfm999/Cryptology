import os
import sys
import fnmatch
from Crypto.PublicKey import RSA
from Crypto.Cipher import AES, PKCS1_OAEP
from Crypto.Util.Padding import unpad
from base64 import b64decode

try:
    with open('key.bin', 'rb') as f:
        priv_key = RSA.import_key(open('ransomprvkey.pem').read())
        enc_key = f.read(priv_key.size_in_bytes())
        f.close()
except OSError as e:
    print(e)

cipher_rsa = PKCS1_OAEP.new(priv_key)
# AES key decrypted
aes_key = cipher_rsa.decrypt(enc_key)

# Write the key back to binary for decryption of 'file.enc'
with open('key.txt', 'wb') as f:
    f.write(aes_key)
    f.close()

# Get the iv from 'iv.pem'
with open('iv.pem', 'rb') as f:
    iv = f.read()

for file in os.listdir('.'):
    if fnmatch.fnmatch(file, '*.enc'):
        with open(file, 'rb') as f:
            data = f.read()
            f.close()
        try:
            dec_iv = b64decode(iv)
            ct = b64decode(data)
            cipher = AES.new(aes_key, AES.MODE_CBC, dec_iv)
            pt = unpad(cipher.decrypt(ct), 32)
            f_dec = file[0:-4] + '.txt'
            with open(f_dec, 'wb') as fw:
                fw.write(pt)
                fw.close()
            with open(f_dec, 'rb') as fr:
                e_data = fr.readlines()
                fr.close()
            with open(f_dec, 'w') as fw:
                for i in range(len(e_data)):
                    fw.write("%s\n" % e_data[i])
                fw.close()
        except ValueError:
            print("Incorrect decryption")
        except KeyError:
            print("Incorrect key")
