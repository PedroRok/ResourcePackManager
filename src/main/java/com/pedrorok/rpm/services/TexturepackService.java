package com.pedrorok.rpm.services;

import com.pedrorok.rpm.repository.TexturepackRepository;
import com.pedrorok.rpm.repository.TexturepackVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Service
@RequiredArgsConstructor
public class TexturepackService {

    private final TexturepackRepository texturepackRepository;
    private final TexturepackVersionRepository versionRepository;
    private final FileStorageService fileStorageService;
    private final VersionService versionService;

}
