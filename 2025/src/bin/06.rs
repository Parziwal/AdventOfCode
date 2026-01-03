advent_of_code::solution!(6);

enum Operator {
    Add,
    Multiply,
}

struct MathProblem {
    numbers: Vec<u64>,
    operator: Operator,
}

impl MathProblem {
    fn solve(&self) -> u64 {
        match self.operator {
            Operator::Add => self.numbers.iter().sum(),
            Operator::Multiply => self.numbers.iter().product(),
        }
    }
}

fn parse_operator(op: &str) -> Operator {
    match op {
        "*" => Operator::Multiply,
        "+" => Operator::Add,
        _ => unreachable!(),
    }
}

fn compute_grand_total(numbers_worksheet: Vec<Vec<u64>>, operator_line: &str) -> u64 {
    numbers_worksheet
        .into_iter()
        .zip(operator_line.split_whitespace())
        .map(|(numbers, operator)| MathProblem {
            numbers,
            operator: parse_operator(operator),
        })
        .map(|math_problem| math_problem.solve())
        .sum()
}

pub fn part_one(input: &str) -> Option<u64> {
    let mut lines_iter = input.lines();
    let operator_line = lines_iter.next_back().unwrap();

    let mut numbers_worksheet: Vec<Vec<u64>> = Vec::new();
    for numbers_line in lines_iter {
        let numbers: Vec<&str> = numbers_line.split_whitespace().collect();
        if numbers_worksheet.is_empty() {
            numbers_worksheet = vec![Vec::new(); numbers.len()]
        }

        for (i, number) in numbers.iter().enumerate() {
            let number: u64 = number.parse().unwrap();
            numbers_worksheet[i].push(number);
        }
    }

    Some(compute_grand_total(numbers_worksheet, operator_line))
}

pub fn part_two(input: &str) -> Option<u64> {
    let mut lines_iter = input.lines();
    let operator_line = lines_iter.next_back().unwrap();

    let worksheet: Vec<Vec<char>> = lines_iter
        .map(|numbers_line| numbers_line.chars().collect())
        .collect();

    let colums_count = worksheet[0].len();
    let mut numbers_worksheet: Vec<Vec<u64>> = vec![Vec::new(); colums_count];
    let mut current_numbers_idx = 0;
    for i in 0..colums_count {
        let number_parts: String = worksheet
            .iter()
            .map(|row| row[i])
            .filter(|c| !c.is_whitespace())
            .collect();

        if !number_parts.is_empty() {
            let number: u64 = number_parts.parse().unwrap();
            numbers_worksheet[current_numbers_idx].push(number);
        } else {
            current_numbers_idx += 1;
        }
    }

    Some(compute_grand_total(numbers_worksheet, operator_line))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(4277556));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(3263827));
    }
}
