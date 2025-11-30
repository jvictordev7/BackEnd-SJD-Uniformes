package br.com.sjduniformes.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImagem(MultipartFile file, String pasta) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("Arquivo muito grande");
        }

        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", pasta);
            options.put("resource_type", "image");
            // Reduz tamanho/qualidade automaticamente mantendo boa resolução.
            Transformation transform = new Transformation()
                .quality("auto:eco")
                .fetchFormat("auto")
                .width(1600)
                .crop("limit");
            options.put("transformation", transform);

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            Object url = uploadResult.get("secure_url");
            if (url == null) {
                throw new RuntimeException("Falha ao obter URL do upload");
            }
            return url.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload para o Cloudinary", e);
        }
    }
}
