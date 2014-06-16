##Reference

This repository contains the source code of the tool developed to extend the OpeNER property-tagger internal resources to a new domain.
The OpeNER property-tagger is the module in charge of labelling some words as being part of a certain property/feature of the reviewed entity.

For example, it should label mentions of "shower" and "towel" as being part of the "bathroom" property of a hotel under review.

In order to do this, the porperty tagger relies on pre-generated mappings between "words" and "properties" (e.g. "shower"->"bathroom", etc.)
These mappings are lists of pairs of words. Of course, these pairs of words are domain and language dependent. The tool contained here aims to ease the adaptation to new domains.

This tool performs two separated steps.
First it reads a (big) set of domain texts. With these texts it creates a word vector space, using the [SemanticVectors](https://code.google.com/p/semanticvectors/) library.

The second step requires a set of user-defined categories (the categories/properties the user wants for this domain). Each category must come with some illustrative words that will serve to define the category and guide the classification process.
Another input is the set of words to find their pairing category/property for this domain (i.e. the words to classify into categories).

The output will be a file with the word-category pairs, that can be used by the property-tagger to work in this new domain.

###Command line interface

In order to invoke the functionality of this tool you need to have Apache Maven in your system.
Then, after going to the directory containing the src folder and the pom.xml file, issue:

```
mvn clean package
```

Now you can execute one of the two processes, generate the word vector space, or classify a list of words into porperties/categories (the second one requires a word vector space).
In order to generate the word vector space the command should like this:

```
java -jar NAME_OF_THE_JAR semvecs [OPTIONS]
```

Where options are:

* -i Path to the directory containing the input KAF files to process
* -o Path to the directory that will contain all the generated output
* -mw Path to the file containing a list of multiword terms that should be treated as a single word when processing the files
* -nd The number of dimensions of the resulting vectors in the word vector space. Default is 200. (See the SemanticVectors library documentation for more about this)
* -nc The number of cycles the algorithm will perform before stopping. Default is 3. (See the SemanticVectors library documentation for more information)

Once you have created a valid word vector space, you can run the classification step:

```
java -jar NAME_OF_THE_JAR classify [OPTIONS]
```

Where options are:

* -lucene Path to the Apache Lucene index (generated during the word vector space creation)
* -vectors Path to the TermVectors file generated during the word vector space creation
* -t Path to the file with the terms you want to classify
* -c Path to the file with the categories you want for this domain (with some example terms representing them)
* -o Path to write the results
* -e Flag to indicate if you want to generate some evaluation information (html table comparing results, confusion matrix, etc.). Note that this requires that the input terms to classify come with a gold category to allow the comparison.
* -h Path to the html file that will be generated to contain the evaluation information

The result will consist on a file containing pairs of words (or multiword terms) and their assigned category for this domain, tab separated.

Obviously, the process and the results obtained using this approach heavily rely on the quality (or representativeness) of the input documents to generate an accurate word vector space.