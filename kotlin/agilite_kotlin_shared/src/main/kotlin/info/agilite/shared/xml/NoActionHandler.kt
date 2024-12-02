package info.agilite.shared.xml

import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException

class NoActionHandler : ErrorHandler {
    override fun error(exception: SAXParseException) {}
    override fun fatalError(exception: SAXParseException) {}
    override fun warning(exception: SAXParseException) {}
}