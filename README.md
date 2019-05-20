Build with:

`mvn clean compile assembly:single`

Run with:

`java -jar target/pipertranscribe-1.0-SNAPSHOT-jar-with-dependencies.jar <file.wav>`

`file.wav` is meant to be a FIFO and a pcm 16 as needed by AWS Transcribe 
and created from `piperpcm.c` by reading a FIFO
and writing to `file.wav`, also a FIFO.

Can we test with just a WAV file for `file.wav`. <- Probably?
