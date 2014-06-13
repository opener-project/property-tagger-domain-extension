##Reference

This repository contains the source code of the tool developed to extend the OpeNER property-tagger internal resources to a new domain.
The OpeNER property-tagger is the module in charge of labelling some words as being part of a certain property/feature of the reviewed entity.

For example, it should label mentions of "shower" and "towel" as being part of the "bathroom" property of a hotel under review.

In order to do this, the porperty tagger relies on pre-generated mappings between "words" and "properties" (e.g. "shower"->"bathroom", etc.)
These mappings are lists of pairs of words. Of course, these pairs of words are domain and language dependent. The tool contained here aims to ease the adaptation to new domains.

This tool performs two separated steps.
First it reads a (big) bunch of domain texts. With these texts it creates a word vector space, using the [SemanticVectors](https://code.google.com/p/semanticvectors/) library.

The second step requires a set of user-defined categories (the categories/properties the user wants for this domain). Each category must come with some illustrative words that will serve to define the category and guide the classification process.
Another input is the set of words to find their pairing category/property for this domain (i.e. the words to classify into categories).

The output will be a file with the word-category pairs, that can be used by the property-tagger to work in this new domain.


## (This documentation is unfinished, work in progress) :-)