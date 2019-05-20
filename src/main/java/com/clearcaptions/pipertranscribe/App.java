package com.clearcaptions.pipertranscribe;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;
import software.amazon.awssdk.services.transcribestreaming.model.MediaEncoding;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionRequest;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * COPYRIGHT:
 *
 * Copyright 2018-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

public class App {
    private static final String endpoint = "https://transcribe.us-west-2.amazonaws.com";
    private static final Region region = Region.of(Region.US_WEST_2.id());
    public static void main(String args[]) throws URISyntaxException, ExecutionException, InterruptedException, LineUnavailableException, FileNotFoundException {
        /**
         * Create Transcribe streaming retry client using AWS credentials.
         */
//        AwsCredentialsProvider creds = StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY));
        TranscribeStreamingRetryClient client = new TranscribeStreamingRetryClient(DefaultCredentialsProvider.create(), endpoint, region);

        StartStreamTranscriptionRequest request =  StartStreamTranscriptionRequest.builder()
            .languageCode(LanguageCode.EN_US.toString())
            .mediaEncoding(MediaEncoding.PCM)
            .mediaSampleRateHertz(44100)
            .build();
        /**
         * Start real-time speech recognition. The Transcribe streaming java client uses the Reactive-streams
         * interface. For reference on Reactive-streams:
         *     https://github.com/reactive-streams/reactive-streams-jvm
         */
        CompletableFuture<Void> result = client.startStreamTranscription(
            /**
             * Request parameters. Refer to API documentation for details.
             */
            request,
            /**
             * Provide an input audio stream.
             * For input from a microphone, use getStreamFromMic().
             * For input from a file, use getStreamFromFile().
             */
            new AudioStreamPublisher(new FileInputStream(new File(args[0]))),
            /**
             * Object that defines the behavior on how to handle the stream
             */
            new StreamTranscriptionBehaviorImpl()
        );

        /**
         * Synchronous wait for stream to close, and close client connection
         */
        result.get();
        client.close();
    }
}

