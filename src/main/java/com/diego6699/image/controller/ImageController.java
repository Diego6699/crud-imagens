package com.diego6699.image.controller;

import com.diego6699.image.model.Image;
import com.diego6699.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public String listImages(Model model) {
        model.addAttribute("images", imageService.getAllImages());
        return "list-images";
    }

    @GetMapping("/upload")
    public String uploadImageForm() {
        return "upload-image";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, Model model) {
        try {
            imageService.saveImage(file);
            model.addAttribute("success", "Imagem carregada com sucesso!");
        } catch (IOException e) {
            model.addAttribute("error", "Erro ao carregar a imagem: " + e.getMessage());
        }
        return "upload-image";
    }

    @GetMapping("/image/{id}")
    public String viewImage(@PathVariable Long id, Model model) {
        Optional<Image> image = imageService.getImageById(id);
        if (image.isPresent()) {
            Image img = image.get();
            String base64Image = Base64.getEncoder().encodeToString(img.getData());
            model.addAttribute("image", img);
            model.addAttribute("base64Image", base64Image);
            return "view-image";
        } else {
            model.addAttribute("error", "Imagem não encontrada!");
            return "redirect:/";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return "redirect:/";
    }
}