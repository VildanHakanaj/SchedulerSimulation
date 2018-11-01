/*======================================================================================================================
|   Simulation of processes of varying length and arrival time on four different scheduling algorithms.
|
|   Name:           ProcessSimulator (main)
|
|   Written by:     Vildan Hakanaj, Joshua Croft - October 2018
|
|   Written for:    COIS 3320 (Prof. Jacques Beland)Lab 2 Trent University Fall 2018.
|
|   Purpose:        To simulate the average turn around time, average arrival time, and number of context switches for
|                   each of the four defined algorithms given 3 different types of job sets.
|
|
|   usage:          Run in any Java IDE
|
|   Subroutines/libraries required:
|       No Subroutines/libraries utilized outside of Java.util.*
|
======================================================================================================================*/

public class Main {// Entry point for the program.
    public static void main(String[] args){
        Simulation sim = new Simulation();
        sim.runSimulation();
    }
}
