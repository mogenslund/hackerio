# Hacker IO

## Deploy
ssh root@134.122.93.102
rm -r hackerio
exit
scp -r ~/proj/hackerio root@134.122.93.102:/root
ssh root@134.122.93.102
screen -S hackerio
cd hackerio/world
./run

CTRL+A D

screen -r hackerio

http://134.122.93.102/
