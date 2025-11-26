import { defineConfig } from "orval";

export default defineConfig({
  shopApi: {
    input: {
      target: "./openapi.json",
    },
    output: {
      mode: "tags-split",
      target: "src/api/generated",
      client: "react-query",
      baseUrl: "http://localhost:8080",
    },
  },
});
