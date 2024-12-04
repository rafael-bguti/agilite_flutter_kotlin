package info.agilite.core.xml

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object ElementXmlConverter {
  private val builder: DocumentBuilder
  private val transformer: Transformer

  init {
    try {
      synchronized(ElementXmlConverter::class.java) {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        factory.isNamespaceAware = true
        factory.isValidating = false
        factory.setFeature("http://xml.org/sax/features/namespaces", false)
        factory.setFeature("http://xml.org/sax/features/validation", false)
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "")
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "")
        builder = factory.newDocumentBuilder()
        builder.setErrorHandler(NoActionHandler())

        transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        transformer.setOutputProperty(OutputKeys.INDENT, "no")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0")
      }
    } catch (e: Exception) {
      throw RuntimeException("Erro ao criar Builder para parser de XML", e)
    }
  }

  fun string2Element(xml: String): ElementXml {
    return synchronized(builder) {
      ElementXml(builder.parse(ByteArrayInputStream(xml.toByteArray(StandardCharsets.UTF_8))))
    }
  }

  fun element2String(element: ElementXml): String {
    val buffer = StringWriter()
    synchronized(transformer) {
      transformer.transform(DOMSource(element.domElement), StreamResult(buffer))
    }
    return buffer.toString()
  }

  fun element2StringTrim(element: ElementXml): String {
    val buffer = StringWriter()
    synchronized(transformer) {
      transformer.transform(DOMSource(element.domElement), StreamResult(buffer))
    }
    return buffer.toString().replace("\n", "").replace("\r", "").replace(">\\s+<".toRegex(), "><")
  }

  fun createElement(rootName: String, namespace: String?): ElementXml {
    var document: Document
    synchronized(builder) { document = builder.newDocument() }
    val root: Element
    if (namespace == null) {
      root = document.createElement(rootName)
    } else {
      root = document.createElementNS(namespace, rootName)
    }
    return ElementXml(root)
  }
}
