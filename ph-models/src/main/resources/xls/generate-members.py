#!/usr/bin/env python

import os
import sys

def main(argv):

    if len(argv) < 1:
        return

    inFile = argv[0]
    print ("Opening: {:s}").format(inFile)

    complexTypesFile = "complexTypes.xsd"
    print ("Writing to: {:s}").format(complexTypesFile)
    open(complexTypesFile, 'w').close()
    complexTypesOut = open(complexTypesFile, 'a')
    write_xsd_header(complexTypesOut)

    elementsFile = "elements.xsd"
    print ("Writing to: {:s}").format(elementsFile)
    open(elementsFile, 'w').close()
    elementsOut = open(elementsFile, 'a')
    write_xsd_header(elementsOut)
    write_include_directive(elementsOut, complexTypesFile)

    with open(inFile, 'rU') as fin:
        for line in fin:
            tokens = line.split(",")
            name_token = tokens[0]
            name_token_normalized = normalize_name(name_token)
            name_token_camel = to_camel_case(name_token_normalized)

            type_token = tokens[1]
            type_token = normalize_type(type_token)

            complexType = complex_type_string(name_token_camel, type_token)
            complexTypesOut.write(complexType)

            element = element_string(name_token_normalized, name_token_camel)
            elementsOut.write(element)

    fin.close()
    write_xsd_footer(complexTypesOut)
    complexTypesOut.close()
    write_xsd_footer(elementsOut)
    elementsOut.close()

def complex_type_string(typeName, typeType):
    node = (
        "<xs:complexType name=\"{:s}\">\n"
            "\t<xs:attribute name=\"value\" type=\"xs:{:s}\" use=\"required\"/>\n"
        "</xs:complexType>\n\n"
    ).format(typeName, typeType)
    return node

def element_string(typeName, typeCamel):
    node = (
        "\t<xs:element name=\"{:s}\" type=\"{:s}\"/>\n"
    ).format(typeName, typeCamel)
    return node


def normalize_name(token):
    token_stripped = token.strip()
    token_stripped = token_stripped.strip("_")
    token = token_stripped.replace(" ", "_")
    return token
    

def normalize_type(token):
    token_stripped = token.strip()
    token_no_space = token_stripped.replace(" ", "")
    token = token_no_space.lower()
    if token == "datetime":
        token = "dateTime"
    return token
    

def to_camel_case(token):
    token = token.lower()
    components = token.split('_')
    # We capitalize the first letter of each component except the first one
    # with the 'title' method and join them together.
    return components[0] + "".join(x.title() for x in components[1:])

def write_include_directive(f, fileName):
    include = (
            "\n\t<xs:include schemaLocation=\"{:s}\"/>\n"
    ).format(fileName)
    f.write(include)

def write_xsd_header(f):
    header = (
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        "<xs:schema elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n\n"
    )
    f.write(header)

def write_xsd_footer(f):
    footer = (
        "\n</xs:schema>\n"
    )
    f.write(footer)


'''

 Python defines special variables before executing the code. 
 For example, if the python interpreter is running the source file as the main program, 
 it sets the special __name__ variable to have a value "__main__". 
 If this file is being imported from another module, __name__ will be set to the module's name.

'''

if __name__ == "__main__":
    main(sys.argv[1:])


