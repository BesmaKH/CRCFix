# CRCFix An automatic program repair tool based on Astor framework that perform a stepwise correctness enhancement process

CRCFix is a program repair tool build using the [Astor]( https://github.com/SpoonLabs/astor/) program repair framework , throw adding new modes of repair that perform unitary increments of correctness enhancement.

 By applying this routine repeatedly we find that we are able in RCFix to find absolutely correct programs or (due to the imperfection of patch generation) we stall before we reach absolutely correct programs (i.e. we find a program that is not absolutely correct but whose correctness we cannot enhance).
 
 CRCFix is a version where we adopt the same generation function of Cardumen. CRCFix is applicable perfectly also on programs containing more than one single site fault or even  multiple site faults. 
#  Contributions made on Astor
The Astor framework gives us the opportunity to integrate our algorithm in a fairly modular fashion while imitating the patch generation process of Cardumen. The later is based on mining code templates from the buggy program.  We than added functionality that applies the concept of relative correctness to asses patches quality. 
The main contributions on Astor are the new modes of repair demonstrated in  the folder  [rcapr](https://github.com/BesmaKH/CRCFix/tree/master/src/main/java/fr/inria/astor/approaches/rcapr) 

The main classes are: 

- IterativeSearchEngine1.java: The main algorithm of our RCFix tool

- RCFixCr.java the version of RCFix where we use Cardumen patch generation function

- RCValidation.java: where we establish oracles of Absolute Correctness, Relative Correctness and Strict Relative correctness

- RCValidationSp.java: used when we specifically use a non deterministic specification

- RCValidator.java: Patch validation that uses relative correctness

The test classes for the find first in sorted experiments are under this [class](https://github.com/BesmaKH/CRCFix/blob/master/src/test/java/fr/inria/astor/test/repair/QuixBugcr_rcfix.java)
 

Our tool can be used effectively in the same way Astor does [Astor Readme File](https://github.com/SpoonLabs/astor/blob/master/README.md/)








