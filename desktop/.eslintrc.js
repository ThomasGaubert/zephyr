module.exports = {
  extends: [
    'standard-with-typescript',
    'prettier'
  ],
  parserOptions: {
    project: './tsconfig.json'
  },
  overrides: [
    {
      files: ['*.ts', '*.tsx'],
      rules: {
        '@typescript-eslint/no-extraneous-class': ['off'],
        '@typescript-eslint/restrict-plus-operands': ['off'],
        '@typescript-eslint/strict-boolean-expressions': ['off']
      }
    },
    {
      files: ['src/reducers/*.ts'],
      rules: {
        '@typescript-eslint/default-param-last': ['off']
      }
    }
  ]
}
