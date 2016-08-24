package cn.edu.buaa.crypto.encryption.ibbe.del07.serialization;

import cn.edu.buaa.crypto.SerializationUtils;
import cn.edu.buaa.crypto.algebra.PairingUtils;
import cn.edu.buaa.crypto.encryption.ibbe.del07.IBBEDel07Engine;
import cn.edu.buaa.crypto.encryption.ibbe.del07.params.IBBEDel07CiphertextParameters;
import cn.edu.buaa.crypto.encryption.ibbe.del07.params.IBBEDel07MasterSecretKeyParameters;
import cn.edu.buaa.crypto.encryption.ibbe.del07.params.IBBEDel07PublicKeyParameters;
import cn.edu.buaa.crypto.encryption.ibbe.del07.params.IBBEDel07SecretKeyParameters;
import cn.edu.buaa.crypto.pairingkem.serialization.PairingParameterXMLSerializer;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.encoders.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.security.InvalidParameterException;

/**
 * Created by Weiran Liu on 2016/8/25.
 *
 * XML sserializer for Delerablée IBBE scheme.
 */
public class IBBEDel07XMLSerializer implements PairingParameterXMLSerializer {
    private static final String TAG_SCHEME_NAME = IBBEDel07Engine.SCHEME_NAME;

    //Tags for public key
    private static final String TAG_PK_W = "W";
    private static final String TAG_PK_V = "V";
    private static final String TAG_PK_HS = "Hs";
    private static final String TAG_PK_HI = "Hi";

    //Tags for master secret key
    private static final String TAG_MSK_G = "G";
    private static final String TAG_MSK_GAMMA = "Gamma";

    //Tags for secret key
    private static final String TAG_SK_ID = "Id";
    private static final String TAG_SK_SK = "sk";

    //Tags for ciphertexts
    private static final String TAG_CT_C1 = "C1";
    private static final String TAG_CT_C2 = "C2";

    private static final IBBEDel07XMLSerializer INSTANCE = new IBBEDel07XMLSerializer();

    private IBBEDel07XMLSerializer() { }

    public static IBBEDel07XMLSerializer getInstance(){
        return INSTANCE;
    }

    public Document documentSerialization(CipherParameters cipherParameters) {
        if (cipherParameters instanceof IBBEDel07PublicKeyParameters) {
            return getInstance().publicKeyParametersSerialization((IBBEDel07PublicKeyParameters) cipherParameters);
        } else if (cipherParameters instanceof IBBEDel07MasterSecretKeyParameters) {
            return getInstance().masterSecretKeyParametersSerialization((IBBEDel07MasterSecretKeyParameters) cipherParameters);
        } else if (cipherParameters instanceof IBBEDel07SecretKeyParameters) {
            return getInstance().secretKeyParametersSerialization((IBBEDel07SecretKeyParameters) cipherParameters);
        } else if (cipherParameters instanceof IBBEDel07CiphertextParameters) {
            return getInstance().ciphertextParametersSerialization((IBBEDel07CiphertextParameters) cipherParameters);
        } else {
            throw new InvalidParameterException("Invalid CipherParameter Instance of " + TAG_SCHEME_NAME +
                    " Scheme, find" + cipherParameters.getClass().getName());
        }
    }

    private Document publicKeyParametersSerialization(IBBEDel07PublicKeyParameters publicKeyParameters){
        try {
            Document publicKeyParametersDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = publicKeyParametersDocument.createElement(IBBEDel07XMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, IBBEDel07XMLSerializer.TYPE_PK);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_LENGTH, Integer.toString(publicKeyParameters.getMaxBroadcastReceiver()));
            publicKeyParametersDocument.appendChild(schemeElement);
            //Set w
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_W, publicKeyParameters.getW());
            //Set v
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_V, publicKeyParameters.getV());
            //Set hs
            SerializationUtils.SetElementArray(publicKeyParametersDocument, schemeElement, TAG_PK_HS, TAG_PK_HI, publicKeyParameters.getHs());
            return publicKeyParametersDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document masterSecretKeyParametersSerialization(IBBEDel07MasterSecretKeyParameters masterSecretKeyParameters) {
        try {
            Document masterSecretKeyDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = masterSecretKeyDocument.createElement(IBBEDel07XMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, IBBEDel07XMLSerializer.TYPE_MSK);
            masterSecretKeyDocument.appendChild(schemeElement);
            //Set g
            SerializationUtils.SetElement(masterSecretKeyDocument, schemeElement, TAG_MSK_G, masterSecretKeyParameters.getG());
            //Set gamma
            SerializationUtils.SetElement(masterSecretKeyDocument, schemeElement, TAG_MSK_GAMMA, masterSecretKeyParameters.getGamma());
            return masterSecretKeyDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document secretKeyParametersSerialization(IBBEDel07SecretKeyParameters secretKeyParameters){
        try {
            Document secretKeyDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = secretKeyDocument.createElement(IBBEDel07XMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, IBBEDel07XMLSerializer.TYPE_SK);
            secretKeyDocument.appendChild(schemeElement);
            //Set Id
            SerializationUtils.SetString(secretKeyDocument, schemeElement, TAG_SK_ID, secretKeyParameters.getId());
            //Set sk
            SerializationUtils.SetElement(secretKeyDocument, schemeElement, TAG_SK_SK, secretKeyParameters.getSecretKey());
            return secretKeyDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document ciphertextParametersSerialization(IBBEDel07CiphertextParameters ciphertextParameters){
        try {
            Document ciphertextDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = ciphertextDocument.createElement(IBBEDel07XMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, IBBEDel07XMLSerializer.TYPE_CT);
            ciphertextDocument.appendChild(schemeElement);
            //Set C1
            SerializationUtils.SetElement(ciphertextDocument, schemeElement, TAG_CT_C1, ciphertextParameters.getC1());
            //Set C2
            SerializationUtils.SetElement(ciphertextDocument, schemeElement, TAG_CT_C2, ciphertextParameters.getC2());
            return ciphertextDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CipherParameters documentDeserialization(PairingParameters pairingParameters, Document document) {
        Element schemeElement = document.getDocumentElement();
        String cipherParameterType = schemeElement.getAttribute(PairingParameterXMLSerializer.ATTRI_TYPE);
        if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_PK)){
            return getInstance().publicKeyParametersDeserialization(pairingParameters, schemeElement);
        } else if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_MSK)){
            return getInstance().masterSecretKeyParametersDeserialization(pairingParameters, schemeElement);
        } else if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_SK)) {
            return getInstance().secretKeyParametersDeserialization(pairingParameters, schemeElement);
        } else if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_CT)) {
            return getInstance().ciphertextParametersDeserialization(pairingParameters, schemeElement);
        } else {
            throw new InvalidParameterException("Illegal " + TAG_SCHEME_NAME + " Document Type, find " + cipherParameterType);
        }
    }

    private CipherParameters publicKeyParametersDeserialization(PairingParameters pairingParameters, Element schemeElement) {
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        int maxLength = Integer.valueOf(schemeElement.getAttribute(PairingParameterXMLSerializer.ATTRI_LENGTH));
        NodeList nodeList = schemeElement.getChildNodes();
        it.unisa.dia.gas.jpbc.Element w = null;
        it.unisa.dia.gas.jpbc.Element v = null;
        it.unisa.dia.gas.jpbc.Element[] hs = new it.unisa.dia.gas.jpbc.Element[maxLength + 1];
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_PK_W)) {
                //Set w
                String wString = node.getFirstChild().getNodeValue();
                w = pairing.getG1().newElementFromBytes(Hex.decode(wString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_PK_V)) {
                //Set v
                String vString = node.getFirstChild().getNodeValue();
                v = pairing.getGT().newElementFromBytes(Hex.decode(vString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_PK_HS)) {
                //Set hs
                NodeList nodeHsList = ((Element) node).getElementsByTagName(TAG_PK_HI);
                for (int j=0; j<nodeHsList.getLength(); j++) {
                    Element elementHi = (Element)nodeHsList.item(j);
                    int index = Integer.valueOf(elementHi.getAttribute(PairingParameterXMLSerializer.ATTRI_INDEX));
                    String hiString = elementHi.getFirstChild().getNodeValue();
                    hs[index] = pairing.getG2().newElementFromBytes(Hex.decode(hiString)).getImmutable();
                }
            }
        }
        return new IBBEDel07PublicKeyParameters(pairingParameters, w, v, hs);
    }

    private CipherParameters masterSecretKeyParametersDeserialization(PairingParameters pairingParameters, Element schemeElement){
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        NodeList nodeList = schemeElement.getChildNodes();
        it.unisa.dia.gas.jpbc.Element g = null;
        it.unisa.dia.gas.jpbc.Element gamma = null;
        for (int i=0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_MSK_G)) {
                //Set g
                String gString = node.getFirstChild().getNodeValue();
                g = pairing.getG1().newElementFromBytes(Hex.decode(gString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_MSK_GAMMA)) {
                //Set gamma
                String gammaString = node.getFirstChild().getNodeValue();
                gamma = pairing.getZr().newElementFromBytes(Hex.decode(gammaString)).getImmutable();
            }
        }
        return new IBBEDel07MasterSecretKeyParameters(pairingParameters, g, gamma);
    }

    private CipherParameters secretKeyParametersDeserialization(PairingParameters pairingParameters, Element schemeElement) {
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        NodeList nodeList = schemeElement.getChildNodes();
        String id = null;
        it.unisa.dia.gas.jpbc.Element sk = null;
        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_SK_ID)) {
                //Set ID
                id = node.getFirstChild().getNodeValue();
            } else if (node.getNodeName().equals(TAG_SK_SK)) {
                //Set sk
                String skString = node.getFirstChild().getNodeValue();
                sk = pairing.getG1().newElementFromBytes(Hex.decode(skString)).getImmutable();
            }
        }
        it.unisa.dia.gas.jpbc.Element elementId = PairingUtils.MapToZr(pairing, id);
        return new IBBEDel07SecretKeyParameters(pairingParameters, id, elementId, sk);
    }

    private CipherParameters ciphertextParametersDeserialization(PairingParameters pairingParameters, Element schemeElement) {
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        NodeList nodeList = schemeElement.getChildNodes();
        it.unisa.dia.gas.jpbc.Element C1 = null;
        it.unisa.dia.gas.jpbc.Element C2 = null;
        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_CT_C1)) {
                //Set C1
                String c1String = node.getFirstChild().getNodeValue();
                C1 = pairing.getG1().newElementFromBytes(Hex.decode(c1String)).getImmutable();
            } else if (node.getNodeName().equals(TAG_CT_C2)) {
                //Set C2
                String c2String = node.getFirstChild().getNodeValue();
                C2 = pairing.getG2().newElementFromBytes(Hex.decode(c2String)).getImmutable();
            }
        }
        return new IBBEDel07CiphertextParameters(pairingParameters, C1, C2);
    }
}