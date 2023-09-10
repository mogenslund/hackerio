
                                 Readme

--------------------------------------------------------------------------------

Mission solution MD5 hashes:

  Mission 1: b3655a22dd7ca6c5710110eb91cf2536
  Mission 2: 74ca39cad0c7a6f4c26d3b8cd0df8d8e

The solution to Mission 1 is your level1 key. So

  echo -n '1966-12-11' | md5sum

Should equal "b3655a22dd7ca6c5710110eb91cf2536" when level1 is replaced with its
value.

There might be cases where a level key is needed to encrypt some content.

TIPS:
  - https://gchq.github.io/CyberChef/
  - Decrypt content of file: ./bin/decrypt my-password </path/to/file.txt

