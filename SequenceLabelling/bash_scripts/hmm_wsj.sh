#!/bin/bash
time java -cp "./mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" \
cc.mallet.fst.HMMSimpleTagger --train true --model-file model_file\
 --test lab $1 $2\
 2> $3 1> $4