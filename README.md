
CRCFix is a program repair tool build based on the [Astor]( https://github.com/SpoonLabs/astor/) program repair framework , throw adding new modes of repair that perform unitary increments of correctness enhancement.
 By applying this routine repeatedly we find that we are able in RCFix to find absolutely correct programs or (due to the imperfection of patch generation) we stall before we reach absolutely correct programs (i.e. we find a program that is not absolutely correct but whose correctness we cannot enhance).
 CRCFix is applicable perfectly also on programs containing more than one single site fault or even  multiple site faults. 
#  Contributions made on Astor
The Astor framework gives us the opportunity to integrate our algorithm in a fairly modular fashion while imitating the patch generation process of Cardumen. The later is based on mining code templates from the buggy program. 
We than added functionality that applies the concept of relative correctness to asses patchs quality. 
The main contributions on Astor are the new modes of repair demonstrated in  the folder  [rcapr] (https://github.com/BesmaKH/CRCFix/tree/master/src/main/java/fr/inria/astor/approaches/rcapr) 
The main classes are: 

-
-
-
-
The test classes for the find first iin sorted experiments are under 
 are unde

Our tool can be used effectively in the same way astor does [] ()astor readme








