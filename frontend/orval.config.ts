import { defineConfig } from "orval";

const isDev = process.env.NODE_ENV !== "production";

const baseURL = isDev
  ? "http://localhost:8080"
  : "https://teamproject007-c48cf54d7f7d.herokuapp.com";
export default defineConfig({
  shopApi: {
    input: {
      target: "./openapi.json",
    },
    output: {
      mode: "tags-split",
      target: "src/api/generated",
      client: "react-query",
      baseUrl: baseURL,
    },
  },
});
