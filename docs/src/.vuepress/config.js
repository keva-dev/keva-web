module.exports = {
  /**
   * Ref：https://v1.vuepress.vuejs.org/config/#title
   */
  title: 'Keva Web',
  /**
   * Ref：https://v1.vuepress.vuejs.org/config/#description
   */
  description: 'Lightweight Java web framework for rapid development in the API era',

  /**
   * Extra tags to be injected to the page HTML `<head>`
   *
   * ref：https://v1.vuepress.vuejs.org/config/#head
   */
  head: [
    ['meta', { name: 'theme-color', content: '#d96666' }],
    ['meta', { name: 'apple-mobile-web-app-capable', content: 'yes' }],
    ['meta', { name: 'apple-mobile-web-app-status-bar-style', content: 'black' }],
    ['meta', { property: 'og:image', content: "/preview.png" }],
    ['link', { rel: "apple-touch-icon", sizes: "180x180", href: "/keva.png"}],
    ['link', { rel: "icon", type: "image/png", sizes: "32x32", href: "/keva.png"}],
    ['link', { rel: "icon", type: "image/png", sizes: "16x16", href: "/keva.png"}],
  ],

  /**
   * Theme configuration, here is the default theme configuration for VuePress.
   *
   * ref：https://v1.vuepress.vuejs.org/theme/default-theme-config.html
   */
  themeConfig: {
    logo: 'https://i.imgur.com/ErsKxIR.png',
    repo: 'https://github.com/keva-dev/keva-web',
    editLinks: false,
    docsDir: '',
    editLinkText: '',
    lastUpdated: false,
    nav: [
      {
        text: 'Guide',
        link: '/guide/',
      },
      {
        text: 'Blogs',
        link: 'https://tuhuynh.com/tags/keva',
      },
      {
        text: 'Team',
        link: '/team/',
      },
    ],
    sidebar: {
      '/guide/': [
        {
          title: 'Overview',
          collapsable: false,
          children: [
            '',
          ]
        },
        'developer-guide'
      ],
    }
  },

  /**
   * Apply plugins，ref：https://v1.vuepress.vuejs.org/zh/plugin/
   */
  plugins: [
    '@vuepress/plugin-back-to-top',
    '@vuepress/plugin-medium-zoom',
  ]
}
