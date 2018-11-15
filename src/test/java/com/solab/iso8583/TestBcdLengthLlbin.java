package com.solab.iso8583;

import com.solab.iso8583.parse.ConfigParser;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.text.ParseException;

public class TestBcdLengthLlbin {

    @Test
    public void shouldSerializeAndDeserializeWithBcdBin() throws IOException, ParseException {
        // Given
        final MessageFactory mf = ConfigParser.createDefault();
        mf.setUseBinaryBitmap(true);
        mf.setUseBinaryMessages(true);

        final IsoMessage isoMessage1 = mf.newMessage(0x1100);

        isoMessage1.setField(2, new IsoValue(IsoType.LLBCDBIN, "1234567890111213"));
        isoMessage1.setField(3, new IsoValue(IsoType.NUMERIC, "000000", "000000".length()));
        isoMessage1.setField(22, new IsoValue(IsoType.ALPHA, "123456", "123456".length()));
        isoMessage1.setField(24, new IsoValue(IsoType.LLLLBCDBIN, "12345678900000000000"));
        isoMessage1.setField(25, new IsoValue(IsoType.LLLBCDBIN, "12345678123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678"));
        isoMessage1.setField(64, new IsoValue(IsoType.BINARY, "1111111111111111", 8));

        // When - Serialize
        final byte[] message1 = isoMessage1.writeData();

        // Then
        Assert.assertEquals("110060000580000000011612345678901112130000003132333435360020123456789000000000000128123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678123456781111111111111111", DatatypeConverter.printHexBinary(message1));

        // When - Deserialize
        mf.setConfigPath("llbcdbin.xml");
        final IsoMessage isoMessage2 = mf.parseMessage(message1, 0);

        // Then
        Assert.assertEquals("LLBCDBIN", isoMessage2.getField(2).getType().name());
        Assert.assertEquals("1234567890111213", isoMessage2.getField(2).toString());
        Assert.assertEquals("000000", isoMessage2.getField(3).toString());
        Assert.assertEquals("123456", isoMessage2.getField(22).toString());
        Assert.assertEquals("LLLLBCDBIN", isoMessage2.getField(24).getType().name());
        Assert.assertEquals("12345678900000000000", isoMessage2.getField(24).toString());
        Assert.assertEquals("LLLBCDBIN", isoMessage2.getField(25).getType().name());
        Assert.assertEquals("12345678123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678", isoMessage2.getField(25).toString());
        Assert.assertEquals("1111111111111111", isoMessage2.getField(64).toString());

        // When - Serialize again
        final byte[] message2 = isoMessage2.writeData();

        // Then
        Assert.assertEquals("110060000580000000011612345678901112130000003132333435360020123456789000000000000128123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678123456781234567812345678123456781111111111111111", DatatypeConverter.printHexBinary(message2));
    }

    @Test
    public void shouldSerializeAndDeserializeWithBcdBinAndOddLength() throws IOException, ParseException {
        // Given
        final MessageFactory mf = ConfigParser.createDefault();
        mf.setUseBinaryBitmap(true);
        mf.setUseBinaryMessages(true);

        final IsoMessage isoMessage1 = mf.newMessage(0x1100);

        // For the LL fields, we use odd lengths in this test
        isoMessage1.setField(2, new IsoValue(IsoType.LLBCDBIN, "12345678901234567"));
        isoMessage1.setField(3, new IsoValue(IsoType.NUMERIC, "000000", "000000".length()));
        isoMessage1.setField(22, new IsoValue(IsoType.ALPHA, "123456", "123456".length()));
        isoMessage1.setField(24, new IsoValue(IsoType.LLLLBCDBIN, "112345678900000000000"));
        isoMessage1.setField(25, new IsoValue(IsoType.LLLBCDBIN, "0123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789"));
        isoMessage1.setField(64, new IsoValue(IsoType.BINARY, "1111111111111111", 8));

        // When - Serialize
        final byte[] message1 = isoMessage1.writeData();

        // Then
        Assert.assertEquals("1100600005800000000117012345678901234567000000313233343536002101123456789000000000000109001234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891111111111111111", DatatypeConverter.printHexBinary(message1));

        // When - Deserialize
        mf.setConfigPath("llbcdbin.xml");
        final IsoMessage isoMessage2 = mf.parseMessage(message1, 0);

        // Then
        Assert.assertEquals("LLBCDBIN", isoMessage2.getField(2).getType().name());
        Assert.assertEquals("012345678901234567", isoMessage2.getField(2).toString());
        Assert.assertEquals("000000", isoMessage2.getField(3).toString());
        Assert.assertEquals("123456", isoMessage2.getField(22).toString());
        Assert.assertEquals("LLLLBCDBIN", isoMessage2.getField(24).getType().name());
        Assert.assertEquals("0112345678900000000000", isoMessage2.getField(24).toString());
        Assert.assertEquals("LLLBCDBIN", isoMessage2.getField(25).getType().name());
        Assert.assertEquals("00123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789", isoMessage2.getField(25).toString());
        Assert.assertEquals("1111111111111111", isoMessage2.getField(64).toString());

        // When - Serialize again
        final byte[] message2 = isoMessage2.writeData();

        // Then
        Assert.assertEquals("1100600005800000000117012345678901234567000000313233343536002101123456789000000000000109001234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891111111111111111", DatatypeConverter.printHexBinary(message2));
    }

}
