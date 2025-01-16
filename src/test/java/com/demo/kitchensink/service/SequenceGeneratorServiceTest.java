package com.demo.kitchensink.service;

import com.demo.kitchensink.model.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SequenceGeneratorServiceTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNextSequence() {
        String seqName = "member";
        Counter counter = new Counter();
        counter.setSeq(42);

        when(mongoOperations.findAndModify(
                eq(Query.query(Criteria.where("_id").is(seqName))),
                any(Update.class),
                any(),
                eq(Counter.class)
        )).thenReturn(counter);

        int result = sequenceGeneratorService.getNextSequence(seqName);

        assertEquals(42, result);
        verify(mongoOperations, times(1)).findAndModify(
                eq(Query.query(Criteria.where("_id").is(seqName))),
                any(Update.class),
                any(),
                eq(Counter.class)
        );
    }

    @Test
    void testGetNextSequenceReturnsDefault() {
        String seqName = "member";

        when(mongoOperations.findAndModify(
                eq(Query.query(Criteria.where("_id").is(seqName))),
                any(Update.class),
                any(),
                eq(Counter.class)
        )).thenReturn(null);

        int result = sequenceGeneratorService.getNextSequence(seqName);

        assertEquals(1, result);
        verify(mongoOperations, times(1)).findAndModify(
                eq(Query.query(Criteria.where("_id").is(seqName))),
                any(Update.class),
                any(),
                eq(Counter.class)
        );
    }
}
