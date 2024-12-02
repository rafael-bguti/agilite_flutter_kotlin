package info.agilite.shared.xml;


import info.agilite.shared.certs.Certificado;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

public class AssinadorXML {
    private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

    public String assinar(String xml, String tagInf, Certificado certificate) throws Exception {
        xml = xml.replace("\n", "").replace("\r", "");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        NodeList elements = doc.getElementsByTagName(tagInf);
        Element el = (Element) elements.item(0);
        el.setIdAttribute("Id", true);
        String id = el.getAttribute("Id");

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        List<Transform> transformList = List.of(
            fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null),
            fac.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null)
        );
        Reference ref = fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA256, null), transformList, null, null);

        SignedInfo si = fac.newSignedInfo(
            fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
            fac.newSignatureMethod(SignatureMethod.RSA_SHA256, null),
            Collections.singletonList(ref)
        );

        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509Certificate cert = certificate.getX509cert();
        List<Object> x509Content = List.of(cert);
        X509Data xd = kif.newX509Data(x509Content);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        PrivateKey privateKey = certificate.getPrivateKey();
        DOMSignContext dsc = new DOMSignContext(privateKey, el.getParentNode());

        XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StringWriter stringWriter = new StringWriter();
        trans.transform(new DOMSource(doc), new StreamResult(stringWriter));

        return stringWriter.getBuffer().toString().replace("\n", "").replace("\r", "");
    }
}
