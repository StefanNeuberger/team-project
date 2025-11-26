

import { useState } from "react";


export default function ProductPhotoUploadButton({ productId }) {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [uploadedUrl, setUploadedUrl] = useState(null);

  const handleUpload = async () => {
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    setLoading(true);

    try {
      const response = await fetch(`/api/photos/item/${productId}`, {
        method: "POST",
        body: formData
      });

      const data = await response.json();
      setUploadedUrl(data.url);
    } catch (err) {
      console.error("Upload failed", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="border-1">
      <input
        className="border-red-700 border-1"
        type="file"
        accept="image/*"
        onChange={e => setFile(e.target.files[0])}
      />

      <button onClick={handleUpload} disabled={!file || loading} className="border-red-700 border-1 ">
        {loading ? "Uploading..." : "Upload Photo"}
      </button>

      {uploadedUrl && (
        <div>
          <p>Uploaded:</p>
          <img
            src={`/api/photos/${uploadedUrl}`}
            alt="Product"
            style={{ width: 150, marginTop: 10 }}
          />
        </div>
      )}
    </div>
  );
}
