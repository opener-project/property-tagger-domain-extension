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

Now you can execute one of the two processes, generate the word vector space, or classify a list of words into properties/categories (the second one requires a word vector space).
In order to generate the word vector space the command should like this:

```
java -jar NAME_OF_THE_JAR semvecs [OPTIONS]
```

Where options are:

* -i Path to the directory containing the input KAF files to process
* -o Path to the directory that will contain all the generated output
* -mw Path to the file containing a list of multiword terms (one per line) that should be treated as a single word when processing the files
* -nd The number of dimensions of the resulting vectors in the word vector space. Default is 200. (See the SemanticVectors library documentation for more about this)
* -nc The number of cycles the algorithm will perform before stopping. Default is 3. (See the SemanticVectors library documentation for more information)

For example (omitting some optional parameters):
```
java -jar target-properties-0.0.1-SNAPSHOT.jar semvecs -i MY_FOLDER_WITH_KAF_FILES -o FOLDER_TO_STORE_RESULTS
```
If no error happens, the indicated output folder will be created and it will contain the resources for the next step.

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

For example (omitting some optional parameters):
```
java -jar target-properties-0.0.1-SNAPSHOT.jar classify -lucene path/to/lucene/index/folder -vectors path/to/termvectors/file -t terms-to-classify.tsv -c defined-categories.tsv -o classification-result.tsv
```

So here we need some of the files generated in the previous step (the semvecs process) plus some extra input files.
The files required from the previous step will at the location stated with the ouput path parameter (-o) when executing the semvecs operation.
One is the generated lucene index (a folder name **lucene_index**). The other is the termvectors file (it will have a name like **termvectorsN.bin** where N is the number of iterations for the algorithm, given as a parameter for the execution command, being 3 by default).

Then the actual input to perform the classification is needed. One is a file with the specification of the desired categories.
It consists of a simple text file containing a category (acting as a mere label) and few words added by hand that should pertain to that category.
The format is tab-separated:
**CATEGORY <TAB> EXAMPLE_WORD_1 <TAB> EXAMPLE_WORD_2 ...**
One line per category definition.

For example (for a restaurant related domain) :
```
food	meat	salad	fish	rice
drink	wine	tea	coke
service	waiter	waitress	bartender
ambiance	atmosphere	romantic	music
price	expensive	money	price	cheap
```

Food, drink, service, ambiance and price (first word of each line) are the categories. The other words are there to serve as an example to define the category. The algorithm will try to assign a category to a new given word based on the semantic similarity to these category-defining words.

The other input file contains the actual words that you want to get classified. They are words from the domain of your interest. The format of the file is the following:

```
sausage	noun	food
burger	noun	food
manager	noun	service
waiter	noun	service
bartender	noun	service
cost	noun	price
affordable	adjective	price
pricey	adjective	price
dollar	noun	price
cool	adjective	ambiance
band	noun	ambiance
jazz	noun	ambiance
attentive	adjective	service
beer	noun	drink
wine	noun	drink
potato	noun	food
chicken	noun	food
```

The format is tab-separated again: **WORD <TAB> Part-of-Speech [<TAB> GOLD_CATEGORY]**
The Part-of-Speech is not used in the process, it is just a label that will be copied to the output, so you can use that information later if needed for a further process.
The GOLD_CATEGORY is optional. If you provide it (annotated by hand) you can invoke the evaluation process (-e flag, plus a path for the html report) that will give you information about the correct and incorrect classifications, just to see how good it performs). Obviously, for a big new vocabulary you don't want to provide such gold categories (otherwise you wouldn't require this tool at all!)

The result will consist of a file with the same format than the terms-to-classify file, but with the automatically assigned categories in the third column.

Obviously, the process and the results obtained using this approach heavily rely on the quality (or representativeness) of the input documents to generate an accurate word vector space.