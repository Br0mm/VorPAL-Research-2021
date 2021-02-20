import java.io.File

class XMLGenerator {

    fun generateXMLFile(fileName: String, directoryName: String, metricValues: List<String>) {
        val metricNames = listOf("AverageOverriddenMethodsPerFile", "AverageFieldsPerClass", "AverageImplementationDepth",
                "MaxImplementationDepth", "MetricA", "MetricB", "MetricC")
        File("$fileName.xml").bufferedWriter().use {
            it.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
            it.newLine()
            it.write("<rootDirectory>")
            it.newLine()
            it.write("\t<name>$directoryName</name>")
            it.newLine()
            it.write("\t<metrics>")
            it.newLine()
            for (i in 0..6) {
                it.write("\t\t<metric>")
                it.newLine()
                it.write("\t\t\t<name>" + metricNames[i] + "</name>")
                it.newLine()
                it.write("\t\t\t<value>" + metricValues[i] + "</value>")
                it.newLine()
                it.write("\t\t</metric>")
                it.newLine()
            }
            it.write("\t</metrics>")
            it.newLine()
            it.write("</rootDirectory>")
        }
    }
}