import processing.core.PImage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
ImageStore: to ideally keep track of the images used in our virtual world
 */

final class ImageStore
{
   private Map<String, List<PImage>> images;
   private List<PImage> defaultImages;

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }

   public Map<String, List<PImage>> getImages()
   {
      return images;
   }

   public List<PImage> getImageList(String key)
   {
      return images.getOrDefault(key, this.defaultImages);
   }
}
