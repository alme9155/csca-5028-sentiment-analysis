#!/bin/bash
./gradlew clean
rm ./components/sentiment/models/distilbert-sst5-finetuned-v3/*.json
rm ./components/sentiment/models/distilbert-sst5-finetuned-v3/model.safetensors
rm ./components/sentiment/models/distilbert-sst5-finetuned-v3/training_args.bin
rm ./components/sentiment/models/distilbert-sst5-finetuned-v3/vocab.txt
echo "All clean!"
