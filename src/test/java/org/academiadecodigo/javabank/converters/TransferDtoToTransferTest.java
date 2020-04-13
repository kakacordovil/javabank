package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.TransferDto;
import org.academiadecodigo.javabank.domain.Transfer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class TransferDtoToTransferTest {

    private TransferDtoToTransfer transferDtoToTransfer;

    @Before
    public void setup() {
        transferDtoToTransfer = new TransferDtoToTransfer();
    }

    @Test
    public void testConvert() {

        //setup
        int fakeSrcId = 9999;
        int fakeDstId = 8888;
        double fakeAmount = 1000.00;

        TransferDto fakeTransferDto = mock(TransferDto.class);

        when(fakeTransferDto.getSrcId()).thenReturn(fakeSrcId);
        when(fakeTransferDto.getDstId()).thenReturn(fakeDstId);
        when(fakeTransferDto.getAmount()).thenReturn(String.valueOf(fakeAmount));

        //exercise
        Transfer transfer = transferDtoToTransfer.convert(fakeTransferDto);

        //verify
        verify(fakeTransferDto, times(1)).getSrcId();
        verify(fakeTransferDto, times(1)).getDstId();
        verify(fakeTransferDto, times(1)).getAmount();

        assertTrue(transfer.getSrcId() == fakeSrcId);
        assertTrue(transfer.getDstId() == fakeDstId);
        assertTrue(transfer.getAmount() == fakeAmount);
    }

}
