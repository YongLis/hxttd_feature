package com.ly.ttd.biz.admin.srv.sequence.impl;

import com.ly.ttd.biz.admin.srv.sequence.LocalSeqService;
import com.ly.ttd.biz.admin.srv.sequence.SequenceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/5/26 14:16
 */
@Slf4j
@Service
public class LocalSeqServiceImpl implements LocalSeqService {
    @Resource
    private SequenceService sequenceService;

    @Override
    public String generateSeq(String prefix, int length, String seqCode) {

        Long seqVal = sequenceService.nextVal(seqCode);

        StringBuilder builder = new StringBuilder(prefix);
        if (length > 0) {
            for (int i = 0; i < length - prefix.length() - seqVal.toString().length(); i++) {
                builder.append("0");
            }
        }
        builder.append(seqVal);
        return builder.toString();
    }
}
