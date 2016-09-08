#!/bin/bash
write_dir=$2
input=$1
for i in `seq 1 10`;
do
        time java -cp "./mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" \
        cc.mallet.fst.SimpleTagger --train true --model-file model_file\
         --training-proportion 0.8 --iterations $3 --test lab --random-seed $i $input\
          2> "${write_dir}test${i}.err" 1> "${write_dir}test${i}.out"
done  