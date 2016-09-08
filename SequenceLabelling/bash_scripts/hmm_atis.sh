#!/bin/bash
write_dir="hmm_atis_none/"
for i in `seq 1 10`;
do
        time java -cp "./mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" \
        cc.mallet.fst.HMMSimpleTagger --train true --model-file model_file\
         --training-proportion 0.8 --iterations $1 --test lab --random-seed $i ../../preprocessed_pos/atis/atis3.pos\
          2> "${write_dir}test${i}.err" 1> "${write_dir}test${i}.out"
done  