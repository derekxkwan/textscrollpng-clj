#!/bin/bash
#edit of vollnixx's script on wordpress
TEXTURE=2048x512
 
#cleanup sheet.png and sheet folder
if [ -e "sheet.png" ];then
    rm sheet.png
fi
 
montage pixout/*.png -background none -tile x1 -geometry "$TEXTURE"+0+0 sheet.png
 
#resize sheet
convert sheet.png -resize 512x128 sheet_rect.png
