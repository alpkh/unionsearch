const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': { // '/api'로 시작하는 모든 요청을 Spring Boot로 프록시 설정
        target: 'http://localhost:8080', // Spring Boot 서버 주소
        changeOrigin: true,  // CORS 문제 방지
        pathRewrite: { '^/api': '' },
      },
    },
  },
});
