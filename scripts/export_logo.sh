#!/bin/bash

cd "$(dirname "$0")" || exit
cd ../docs/assets/images/logos/ || exit

if [ -f "inflamity.png" ]; then
  echo "Logo already exists. Skipping processing."
  exit 0
fi

gimp -n -i -b - <<EOF
(let* (
  (scale-factor 10.0)
  (file's (cadr (file-glob "*.xcf" 1)))
  (filename "")
  (image 0)
  (layer 0)
  (width 0)
  (height 0)
)
  (while (pair? file's)
    (set! image (car (gimp-file-load RUN-NONINTERACTIVE (car file's) (car file's))))
    (set! layer (car (gimp-image-merge-visible-layers image CLIP-TO-IMAGE)))

    ;; Get image dimensions
    (set! width (car (gimp-image-width image)))
    (set! height (car (gimp-image-height image)))

    ;; Scale the image with no interpolation
    (gimp-image-scale-full image (* width scale-factor) (* height scale-factor) INTERPOLATION-NONE)

    (set! filename (string-append (substring (car file's) 0 (- (string-length (car file's)) 4)) ".png"))
    (gimp-file-save RUN-NONINTERACTIVE image layer filename filename)

    (gimp-image-delete image)
    (set! file's (cdr file's))
  )
  (gimp-quit 0)
)
EOF

cd ../../../../ || exit

cp ./docs/assets/images/logos/inflamity.png ./.idea/icon.png
