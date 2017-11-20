package com.sanket.itunespodcast;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sanket on 10/3/16.
 */
public class Podcast implements Serializable,  Comparable<Podcast>{


        private String title;
        private String podcastDescription;
        private String newsFeedLink;
        private Date updatedDate;
        private String thumbnailImageUrl;
        private String largeImageUrl;

    public Boolean isGreen() {
        return isGreen;
    }

    public void setIsGreen(Boolean green) {
        isGreen = green;
    }

    private Boolean isGreen;


        public String getLargeImageUrl() {
            return largeImageUrl;
        }

        public void setLargeImageUrl(String largeImageUrl) {
            this.largeImageUrl = largeImageUrl;
        }

        public Podcast() {
            this.isGreen=false;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPodcastDescription() {
            return podcastDescription;
        }

        public void setPodcastDescription(String podcastDescription) {
            this.podcastDescription = podcastDescription;
        }

        public String getNewsFeedLink() {
            return newsFeedLink;
        }

        public void setNewsFeedLink(String newsFeedLink) {
            this.newsFeedLink = newsFeedLink;
        }

        public Date getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(Date updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getThumbnailImageUrl() {
            return thumbnailImageUrl;
        }

        public void setThumbnailImageUrl(String thumbnailImageUrl) {
            this.thumbnailImageUrl = thumbnailImageUrl;
        }

    @Override
    public int compareTo(Podcast another) {
        return 0;
    }
}


