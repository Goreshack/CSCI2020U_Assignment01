Original can be found at:
http://csundergrad.science.uoit.ca/courses/csci2020u/assignments/assignment1.pdf

Of note, this is essentially a regurgitation of the information linked above,
this document is merely to reinforce the idea to myself.


Basic Idea of Assignment 1 is as follows:

Using machine learning we will attempt to utilize training and test data to
differentiate between "spam" and "ham" -- unwanted and wanted emails
respectively.

The training and test data (hereinafter TRD and TSTD -- TRaining Data
&& TeST Data) consists of two folders aptly named "test" and "train."

The breakdown of these folders:

Train:

- ham{
  2500 emails
  Considered wanted
}

- ham2{
  250 emails
  Also wanted
}
- spam{
  500 emails
  Unwanted messages -- to be filtered out when tested.
}

Test:

- ham{
  1400 emails
  Wanted
}
- spam {
  1400 emails
  Unwanted
}

In the training phase for the program, we will determine which words exist in
each file. Using this information we will create two frequency maps,
trainHamFreq and trainSpamFreq. These will represent as their names suggest.

This data will be used to try and predict whether or not a file is spam.
A simplified probability is given as Pr(Wi | H).

Further:: Pr(S | Wi) = (Pr(Wi | S))/(Pr(Wi | S) + Pr(Wi | H)) -- Wi indicating
a given word, S for spam and H for ham.

As for testing, once we have built a probability map we will use our prior
knowledge to analyze test/ham and test/spam to try our detection program
-- analyzing each word.

Ham and spam probabilities will be used to predict whether or not a given file
is spam.

We will use the following equations:


Legend:
Incapsulated within the brackets the notation will be as follows;

E denotes summation of
ln indicates the natural logarithm
e denotes euler's constant
^ indicates to the power of

{

n = E (ln( 1 - Pr(S | Wi)) - ln(Pr(S | Wi)))
Pr(S | F) = 1 / (1 + e^n)

}

Once we have obtained this result, we must display them in a Tableview which
will contain several columns;

Filename
Spam Detector's Categorization (from our previous results.)
Actual Category (we know this based upon it's folder placement)

At the bottom of this window we must display some summary stats, including the
percentage of correct guesses (accuracy) and ratio of correct positives (spam)
to spam guesses (precision).

Equations:

accuracy = numCorrectGuesses/numGuesses

precision = numTruePositives / (numFalsePositives + numTruePositives).

We are also tasked with improving upon the detector.

I question the authenticity of this assignment due to the lack of words like
"inches", "massive" and "growth."

The spam doesn't feel totally realistic without. 
# CSCI2020U_Assignment1_100491442
