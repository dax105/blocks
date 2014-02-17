/**
 Copyright (c) 2012, Kevin Sacro
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 Redistributions in binary form must reproduce the above copyright notice, this
 list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
**/

package dax.blocks.world;

import java.util.Arrays;


/**
 * Uses the Diamond-Square Algorithm to generate height map data. Generated
 * data is returned in a 2D array of doubles.
 * 
 * @author Kevin Sacro
 */
public class HeightMapGenerator {

        // the generated map's height and width will be equal to gensize
        private int gensize;
        private int width;
        private int height;
        private double variance;
        
        /**
         * Lone constructor.
         */
        public HeightMapGenerator(){
                
                gensize = (int)Math.pow(2, 9) + 1;
                width = gensize;
                height = gensize;
                variance = 1;
        }
        
        /**
         * Adjusts the height map dimensions.
         * @param width - The desired width (in pixels) of generated maps.
         * @param height - The desired height (in pixels) of generated maps.
         */
        public void setSize(int width, int height){
                
                this.width = width;
                this.height = height;
                
                // gensize must be in the form 2^n + 1 and
                // also be greater or equal to both the width
                // and height
                double w = Math.ceil(Math.log(width)/Math.log(2));
                double h = Math.ceil(Math.log(height)/Math.log(2));
                
                if(w > h){
                        gensize = (int)Math.pow(2, w) + 1;
                }
                else{
                        gensize = (int)Math.pow(2, h) + 1;
                }
        }

        
        /**
         * Adjusts the height map dimensions.
         * @param n - Sets the width and height of generated maps to be 2^n + 1 pixels.
         */
        public void setGenerationSize(int n){
                
                gensize = (int)Math.pow(2, n) + 1;
                width = gensize;
                height = gensize;
        }
        
        /**
         * @param v - The higher the variance, the rougher
         * the height map. By default it is 1.
         */
        public void setVariance(double v){
                variance = v;
        }
        
        /**
         * Generates height map data and places it in a 
         * 2D array. A new height map is created everytime
         * generate() is called.
         * 
         * @return A 2D array containing height map data.
         */
        public double[][] generate() {

                double[][] map = new double[gensize][gensize];

                // Place initial seeds for corners
                map[0][0] = Math.random();
                map[0][map.length - 1] = Math.random();
                map[map.length - 1][0] = Math.random();
                map[map.length - 1][map.length - 1] = Math.random();

                map = generate(map);
                
                if(width < gensize || height < gensize){
                        
                        double[][] temp = new double[width][height];
                        
                        for(int i = 0; i < temp.length; i++){
                                temp[i] = Arrays.copyOf(map[i], temp[i].length);
                        }
                        
                        map = temp;
                        
                }
                
                return map;

        }
        
        /**
         * Fills the specified 2D array with height map data and returns it.
         * Any index whose value is not 0 will not be overwritten and used
         * during procedural generation. This makes this method ideal for
         * pre-seeding data to generate maps with specific features.
         * 
         * @param map - 2D array containing height map data
         * 
         * @return A 2D array containing height map data.
         */
        public double[][] generate(double[][] map){
                
                map = map.clone();
                int step = map.length - 1;

                double v = variance;
                
                while(step > 1){

                        // SQUARE STEP
                        for(int i = 0; i < map.length - 1; i += step){
                                for(int j = 0; j < map[i].length - 1; j += step){

                                        double average = (map[i][j] + map[i + step][j] + map[i][j + step] + map[i+step][j+step])/4;

                                        if(map[i + step/2][j + step/2] == 0) // check if not pre-seeded
                                                map[i + step/2][j + step/2] = average + randVariance(v);

                                }       
                        }

                        // DIAMOND STEP
                        for(int i = 0; i < map.length - 1; i += step){
                                for(int j = 0; j < map[i].length - 1; j += step){

                                        if(map[i + step/2][j] == 0) // check if not pre-seeded
                                                map[i + step/2][j] = averageDiamond(map, i + step/2, j, step) + randVariance(v);

                                        if(map[i][j + step/2] == 0)
                                                map[i][j + step/2] = averageDiamond(map, i, j + step/2, step) + randVariance(v);

                                        if(map[i + step][j + step/2] == 0)
                                                map[i + step][j + step/2] = averageDiamond(map, i + step, j + step/2, step) + randVariance(v);

                                        if(map[i + step/2][j + step] == 0)
                                                map[i + step/2][j + step] = averageDiamond(map, i + step/2, j + step, step) + randVariance(v);

                                }       
                        }

                        v /=2;
                        step /= 2;
                }
                
                return map;
        }

        private double averageDiamond(double[][] map, int x, int y, int step){

                int count = 0;
                double average = 0;

                if(x - step/2 >= 0){
                        count++;
                        average += map[x - step/2][y];
                }

                if(x + step/2 < map.length){
                        count++;
                        average += map[x + step/2][y];
                }

                if(y - step/2 >= 0){
                        count++;
                        average += map[x][y - step/2];
                }

                if(y + step/2 < map.length){
                        count++;
                        average += map[x][y + step/2];
                }

                return average/count;
        }

        private double randVariance(double v){
                return Math.random()*2*v - v;
        }

}